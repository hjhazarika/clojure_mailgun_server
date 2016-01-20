(ns clojure-mailgun-server.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [cheshire.core :as json]
            [clojure-mailgun-server.mailer.mailgun :as mailgun]
            [liberator.representation :refer [ring-response]])
  (:import [clojure_mailgun_server.mailer.mailgun EmailRequest]))

(def ^:private allowed-templates #{"welcome.html" "password_reset.html"} )


(defresource email [to subject template]
  :available-media-types ["application/json"]
  :allowed-methods [:post]
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
