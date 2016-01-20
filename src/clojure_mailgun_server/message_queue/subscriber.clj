(ns clojure-mailgun-server.message-queue.subscriber
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.consumers :as lc]
            [clojure-mailgun-server.mailer.mailgun :as mailgun]
            [cheshire.core :as json]
            [clojure.walk :as walk]
            [clojure.java.io :as io]
            [langohr.basic :as lb])
  (:import [clojure_mailgun_server.mailer.mailgun EmailRequest]))

(def ^{:const true}
  default-exchange-name "")

(defn message-handler
  [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  (let [msg (json/parse-stream (io/reader payload))
        msg# (walk/keywordize-keys msg)
        email-request (EmailRequest. (:to msg#) (:subject msg#) (:template msg#) (:values msg#))]
    (timbre/info "[Processing email for Subject through MQ" (:subject email-request) " to " (:to email-request)
    (mailgun/send-email email-request)))
(defn -main
  [& args]
  (let [conn (rmq/connect)
        ch (lch/open conn)
        qname "clojure-mailgun-server.message-queue"]
    (println (format "[main] subscriber Connected. Channel id: %d" (.getChannelNumber ch)))
    (lq/declare ch qname {:exclusive false :auto-delete true})
    (lc/subscribe ch qname message-handler {:auto-ack true})))