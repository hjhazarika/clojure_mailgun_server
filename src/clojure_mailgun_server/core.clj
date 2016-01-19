(ns clojure-mailgun-server.core
  (:require [liberator.core :refer [resource defresource]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes ANY]]
            [cheshire.core :as json]
            [liberator.representation :refer [ring-response]]))

(defroutes app
  ;;Route to check if the server is up and running
  (ANY "/ping" [] (resource
                    :available-media-types ["application/json"]
                    :handle-ok (fn [_] (ring-response {:body (json/generate-string "pong")})))))

(def handler
  (-> app
    wrap-params))
