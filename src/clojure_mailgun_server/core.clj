(ns clojure-mailgun-server.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [cheshire.core :as json]
            [buddy.auth :as buddy]
            [clojure-mailgun-server.mailer.mailgun :as mailgun]
            [buddy.auth.http :as http]
            [buddy.core.codecs :refer [base64->str]]
            [cuerdas.core :as str]
            [liberator.representation :refer [ring-response]])
  (:import [clojure_mailgun_server.mailer.mailgun EmailRequest]))

(def ^:private allowed-templates #{"welcome.html" "password_reset.html"} )

(defn- parse-httpbasic-header
  "Given a request, try extract and parse
  http basic header."
  [request]
  (let [pattern (re-pattern "^Basic (.+)$")
        decoded (some->> (http/-get-header request "authorization")
                  (re-find pattern)
                  (second)
                  (base64->str))]
    (when-let [[username password] (str/split decoded #":" 2)]
      {:username username :password password})))



;;choice was between fullfledged immutant or basic compojure and liberator,
;; liberator has most of the possible workflows for any webservice inbuilt and hence picked it up
;; to /subject and template were made path params as they make for better debugging and also easy rejection of requestions
;; without processing the entire request body
;; hashmap for the template was put in request body as this is something that might increase in size later


(defn authorized? [ctx]
  (let [{username :username password :password} (parse-httpbasic-header (:request ctx))]
    (and (= username "admin") (= password "admin") )))

(defresource email [to subject template]
  :available-media-types ["application/json"]
  :allowed-methods [:post]
  :authorized?  authorized?
  :processable? (fn [ctx]
                  (allowed-templates template)) ;;only valid templates should proceed
  :post! (fn [ctx]
           (let [body (get-in ctx [:request :body])
                 body# (slurp body)
                 val-map (json/parse-string body#)
                 val-map# (clojure.walk/keywordize-keys val-map)
                 email-reqeust (EmailRequest. to subject template val-map#)]
             (mailgun/send-email email-reqeust)))
  :handle-unprocessable-entity (fn [_]
                                 (ring-response {:body (json/generate-string "cannot process ! Template does not exist in repo")}) )
  :handle-created  (fn [_]
                 (ring-response {:body (json/generate-string "emailed")})))


(defroutes app
  ;;Route to check if the server is up and running
  (ANY "/ping" [] (resource
                    :available-media-types ["application/json"]
                    :handle-ok (fn [a] (ring-response {:body (json/generate-string "pong")}))))
  (ANY "/email/:to{.+@.+}/:subject/:template"
    [to subject template]
    (email to subject template) ))



(def handler
  (-> app
    wrap-params))
