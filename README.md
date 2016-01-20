# clojure_mailgun_server

A Clojure library designed to take in a request over http or queue and send mails

## Usage

lein ring server-headless

lein run -m clojure-mailgun-server.mailer.mailgun # send a mail to me

lein run test run test cases

curl -uadmin:admin localhost:3000/email/hjhazarika@gmail.com/subject/welcome.html  -d '{"name":"himangshu","search":"google"}' // a valid curl request


lein run -m clojure-mailgun-server.message-queue.publisher //run message queue publisher
 
lein trampoline run //run message queue subscriber

## Prerequisites
need to install rabbitmq
need to install mongodb



