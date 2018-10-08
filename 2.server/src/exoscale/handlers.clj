(ns exoscale.handlers
  (:require
   [exoscale.spec :refer [wrap-spec-in]]
   [exoscale.storage :as storage]))


(defn list-jobs
  [request]
  {:status 200
   :body (or (storage/list-jobs) [])})


(defn _put-job
  [request]
  (let [{:keys [params]} request
        job (storage/put-job params)]
    {:status 200
     :body job}))


(def put-job
  (wrap-spec-in _put-job :exoscale.spec/put-job))


(defn _delete-job
  [request]
  (let [{:keys [params]} request
        {:keys [id]} params]
    (storage/delete-job id)
    {:status 200
     :body (or (storage/list-jobs) [])}))


(def delete-job
  (wrap-spec-in _delete-job :exoscale.spec/delete-job))


(defn not-found
  [request]
  {:status 404
   :body {:error "NOT_FOUND"}})
