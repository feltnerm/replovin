(ns overtone-server
  (:require [com.stuartsierra.component :as component]
            [overtone.core :refer [boot-server-and-mixer]]))

(defrecord OvertoneServer []
  component/Lifecycle

  (start [component]
    (println "Starting Overtone")
    (if (overtone.sc.server/server-disconnected?)
      (let [conn (boot-server-and-mixer)]
        (assoc component :otone conn))
      component))

  (stop [component]
    (println "Stopping Overtone")
    (if (overtone.sc.server/server-connected?)
      (do
        (overtone.sc.server/kill-server)
        (assoc component :otone nil))
      component)))

(defn new-overtone-server []
  (map->OvertoneServer {}))

(defn overtone-system []
  (component/system-map
   :otone (new-overtone-server)))
