(ns user)

(println "User!")
(defn repl
  "Load and switch to the 'live' namespace."
  []
  (require 'repl)
  (in-ns 'repl))
