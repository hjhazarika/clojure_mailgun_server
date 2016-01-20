(ns clojure-mailgun-server.mailer.selmer
  (:require [selmer.parser :as parser]))
;;templating apis for this repo
(parser/set-resource-path! (clojure.java.io/resource "templates"))

(defn generate-content "inserts the value map in template and gives string content"
  [^String template-name ^java.util.Map value-map]
  (parser/render-file template-name value-map))
