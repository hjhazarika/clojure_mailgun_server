(ns clojure-mailgun-server.mailer.mailgun-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [org.httpkit.fake :refer :all]
            [clojure-mailgun-server.mailer.mailgun :refer :all]
            [cheshire.core :as json])
  (:import [clojure_mailgun_server.mailer.mailgun EmailRequest]))

(deftest test-mail-sent
  (swap! mode (fn [_] "test"))
  (testing "Mail sent successfully"
    (is
      (let [resp (with-fake-http
                   [{:url "https://api.mailgun.net/v3/sandbox04a729f1bbf64a1891d3cffb7cfdcca3.mailgun.org/messages"
                                   :method :post} "correct url"]
                   (send-email (EmailRequest. "hjhazarika@gmail.com" "hello world" "welcome.html"
                                 {:name "himangshu" :search "google"})))]
        (= @resp "correct url"))))

  (testing "invalid email"
    (is
      (let [resp (with-fake-http
                   [{:url "https://api.mailgun.net/v3/sandbox04a729f1bbf64a1891d3cffb7cfdcca3.mailgun.org/messages"
                     :method :post} "correct url"]
                   (send-email "invalid request")
                   )]
        (= nil resp)))))