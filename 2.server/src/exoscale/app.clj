(ns exoscale.app
  (:require
   [compojure.core :refer
    [context defroutes ANY GET POST DELETE]]

   [ring.middleware.keyword-params
    :refer [wrap-keyword-params]]

   [ring.middleware.json
    :refer
    [wrap-json-response
     wrap-json-params]]

   [exoscale.handlers :as handlers]))


(defroutes app-naked

  (context
   "/jobs" []

   (GET "/" request (handlers/list-jobs request))

   (POST "/" request (handlers/put-job request))

   (context
    "/:id" []

    (DELETE "/" request (handlers/delete-job request))))

  handlers/not-found)


(def app
  (-> app-naked
      wrap-keyword-params
      wrap-json-params
      wrap-json-response))
