(ns clojure-mailgun-server.db.mailer-audit

  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(defn- get-db []
  (let [conn (mg/connect)]
    (mg/get-db conn "mailer_audit")))

(defn insert-data "inserts mailgun response to db for logging" [^java.util.Map mailgun-response]
  (mc/insert-and-return (get-db) "mailgun_logs" mailgun-response))