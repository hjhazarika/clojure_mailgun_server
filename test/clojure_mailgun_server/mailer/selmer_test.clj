(ns clojure-mailgun-server.mailer.selmer-test
  (:require [clojure.test :refer :all]
            [clojure.string :as string]
            [clojure-mailgun-server.mailer.selmer :refer :all]
            [cheshire.core :as json]))

(deftest content-generation
  (testing "welome"
    (is
      (. (generate-content "welcome.html" {:name "himangshu" :search "google"}) (contains "himangshu"))))
  (testing "reset"
    (is
      (. (generate-content "password_reset.html" {:name "himangshu" :password "password"}) (contains "password")))))