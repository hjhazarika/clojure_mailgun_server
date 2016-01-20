(defproject clojure_mailgun_server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler clojure-mailgun-server.core/app}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [liberator "0.13"]
                 [compojure "1.4.0"]
                 [cheshire "5.5.0"]
                 [selmer "1.0.0"]
                 [buddy/buddy-auth "0.9.0"]
                 [com.novemberain/langohr "3.5.0"]
                 [http-kit.fake "0.2.1"]
                 [http-kit "2.1.18"]
                 [ring/ring-core "1.4.0"]]
  :main clojure-mailgun-server.message-queue.subscriber)
