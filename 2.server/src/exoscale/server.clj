(ns exoscale.server
  (:require
   [exoscale.app :refer [app]]
   [exoscale.config :refer [config]]

   [ring.adapter.jetty :refer [run-jetty]]))


(defonce server nil)


(defn alter-server [srv]
  (alter-var-root #'server (constantly srv)))


(defn start []
  (let [{:keys [port]} config
        opt {:port port :join? false}
        server (run-jetty #'app opt)]
    (alter-server server)))


(defn stop []
  (when server
    (.stop ^org.eclipse.jetty.server.Server server)
    (alter-server nil)))
