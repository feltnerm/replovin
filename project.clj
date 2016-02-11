(defproject replovin "0.1.0-SNAPSHOT"
 :description "REPLovin' -- a live-coding project"
 :url "http://github.com/feltnerm/replovin"
 :license {:name "Eclipse Public License"
           :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.stuartsierra/component "0.3.1"]
                 [overtone "0.9.1"]]
  :profiles {
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   :source-paths ["live"]}
             :uberjar {:aot :all}})
