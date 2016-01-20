(ns clojure-mailgun-server.message-queue.publisher
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [cheshire.core :as json]
            [langohr.basic     :as lb]))

(def ^{:const true}
  default-exchange-name "")
(defn -main
  [& args]
  (let [conn  (rmq/connect)
        ch    (lch/open conn)
        qname "clojure-mailgun-server.message-queue"]
    (println (format "[main] Connected. Channel id: %d Publishing message" (.getChannelNumber ch)))
    (lq/declare ch qname {:exclusive false :auto-delete true})
    (lb/publish ch default-exchange-name qname
      (json/generate-string {:to "hjhazarika@gmail.com" :subject "hello" :template "welcome.html" :values {:name "himangshu" :search "google"}} )
    {:content-type "text/plain" :type "email_request"})
    (Thread/sleep 2000)
    (println "[main] Disconnecting...")
    (rmq/close ch)
    (rmq/close conn)))