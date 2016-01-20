(ns clojure-mailgun-server.mailer.mailgun
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure-mailgun-server.db.mailer-audit :as mailer-audit]
            [clojure-mailgun-server.mailer.selmer :as parser]))


(defrecord EmailRequest [to subject template value-map])

(defn send-email "takes an email request and makes a get call to mailgun server, returns null for invalid request"
  [^EmailRequest request]
  (if (instance? EmailRequest request)
    (let [content (parser/generate-content (:template request) (:value-map request)) ]
      (http/post "https://api.mailgun.net/v3/sandbox04a729f1bbf64a1891d3cffb7cfdcca3.mailgun.org/messages"
        {:user-agent "hjh-clojure-client"
         :form-params {"from" "Mailgun testing himangshu <postmaster@sandbox04a729f1bbf64a1891d3cffb7cfdcca3.mailgun.org>"
                       "to" (:to request)
                       "subject" (:subject request)
                       "text" content }
         :basic-auth ["api" "key-d01395648c72b4f4affc5361421245c1"]}
        (fn [{:keys [status headers body error opts]}] ;;callback function to persist status in db other approaches include using a redis or message queue
             (let [body# (json/parse-string body)
             body# (walk/keywordize-keys body#)
             id (:id body#)
             id# (string/replace id #"<|>" "")]
          (mailer-audit/insert-data {:to (:to request)
                                     :subject (:subject request)
                                     :content content
                                     :resp_id id#})))
          ))))

(defn -main []
  (println @(send-email (EmailRequest. "hjhazarika@gmail.com" "hello world" "welcome.html" {:name "himangshu" :search "google"}))))