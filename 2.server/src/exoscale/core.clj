(ns exoscale.core
  (:require
   [exoscale.storage :as storage]
   [exoscale.server :as server])

  (:gen-class))


(defn start
  []
  (storage/start)
  (server/start))


(defn stop
  []
  (server/stop)
  (storage/stop))


(defn -main
  [& args]
  (start)
  (println "OK. Type (exoscale.core/stop) to halt."))
