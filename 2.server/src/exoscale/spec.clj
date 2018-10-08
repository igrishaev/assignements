(ns exoscale.spec
  (:refer-clojure :exclude [uuid?])
  (:require
   [clojure.spec.alpha :as s]
   [clojure.string :as str]))


(def invalid :clojure.spec.alpha/invalid)


(def not-empty-string
  (s/and
   string?
   (s/conformer str/trim)
   not-empty))


(s/def ::company not-empty-string)

(s/def ::title not-empty-string)

(s/def ::description not-empty-string)


(def uuid?
  (partial re-matches #"^\w{8}-\w{4}-\w{4}-\w{4}-\w{12}$"))


(s/def ::id uuid?)


(s/def
  ::put-job
  (s/keys :req-un
          [::company ::title ::description]))

(s/def
  ::delete-job
  (s/keys :req-un [::id]))


(defn wrap-spec-in
  [handler spec]
  (fn [request]

    (let [{:keys [params]} request
          params (s/conform spec params)
          ok? (not= params invalid)]

      (if ok?
        (handler (assoc request :params params))
        {:status 400
         :body {:error "BAD_INPUT_PARAMS"}}))))
