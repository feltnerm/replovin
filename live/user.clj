(ns user)

(println "User!")
(defn live
  "Load and switch to the 'live' namespace."
  []
  (require 'live)
  (in-ns 'live))
