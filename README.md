# clojure_mailgun_server

A Clojure library designed to take in a request over http or queue and send mails

## Usage

lein ring server-headless

lein run -m clojure-mailgun-server.mailer.mailgun # send a mail to me

lein run test run test cases