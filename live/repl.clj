(ns repl
  (:require [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [com.stuartsierra.component :as component]
            [overtone-server]))

;;; used to start/stop overtone server
(def system nil)

(defn init
  []
  (alter-var-root #'system
                  (constantly (overtone-server/overtone-system))))

(defn start
  []
  (alter-var-root #'system component/start))

(defn stop
  []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go
  []
  (init)
  (start)
  :ready)

(defn reset
  []
  (stop)
  (refresh :after 'repl/go))

(defn play
  []
  (go)
  (require 'live)
  (in-ns 'live))
