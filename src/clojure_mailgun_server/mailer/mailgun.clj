(ns clojure-mailgun-server.mailer.mailgun
  (:require [org.httpkit.client :as http]))


(defrecord EmailRequest [to subject body])

(defn send-email "takes an email request and makes a get call to mailgun server, returns null for invalid request"
  [^EmailRequest request]
  (if (instance? EmailRequest request)
    (http/post "https://api.mailgun.net/v3/sandbox04a729f1bbf64a1891d3cffb7cfdcca3.mailgun.org/messages"
      {:user-agent "hjh-clojure-client"
       :form-params {"from" "Mailgun testing himangshu <postmaster@sandbox04a729f1bbf64a1891d3cffb7cfdcca3.mailgun.org>"
                     "to" (:to request)
                     "subject" (:subject request)
                     "text" (:body request)}
       :basic-auth ["api" "key-d01395648c72b4f4affc5361421245c1"]})))

(defn -main []
  (println @(send-email (EmailRequest. "hjhazarika@gmail.com" "hello world" "testing mailgun server"))))