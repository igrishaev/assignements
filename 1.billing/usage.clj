(ns metered.usage
  (:import java.util.Date))


(def events
  [{:usage/event     :usage.event/create
    :usage/resource  :usage.resource/object
    :usage/uuid      #uuid "d8377d93-db71-488a-b894-54a962760bea"
    :usage/account   #uuid "ee12577c-983f-4729-a0e9-c5789a906c04"
    :usage/timestamp #inst "2017-03-10T00:00:00.000-00:00"}
   {:usage/event     :usage.event/destroy
    :usage/resource  :usage.resource/object
    :usage/uuid      #uuid "d8377d93-db71-488a-b894-54a962760bea"
    :usage/account   #uuid "ee12577c-983f-4729-a0e9-c5789a906c04"
    :usage/timestamp #inst "2017-03-10T01:00:00.000-00:00"}])


(defn abs
  [^Number n]
  (if (neg? n)
    (- n) n))


(defn date-diff
  "Returns a difference b/w two dates in minutes."
  [^Date date1 ^Date date2]
  (quot
   (abs
    (- (.getTime date1)
       (.getTime date2)))
   (* 60 1000)))


(defn process-usage
  [events]

  (let [criteria (juxt :usage/account :usage/uuid)
        grouped (group-by criteria events)]

    (for [[[account uuid] events] grouped]

      (let [[e1 e2] events

            date1 (:usage/timestamp e1)
            date2 (:usage/timestamp e2)

            diff (when (and date1 date2)
                   (date-diff date2 date1))

            resource (-> events first :usage/resource)]

        {:usage/resource  resource
         :usage/uuid      uuid
         :usage/account   account
         :usage/duration  diff}))))


#_
(assert (= (process-usage events)
           [{:usage/resource  :usage.resource/object
             :usage/uuid      #uuid "d8377d93-db71-488a-b894-54a962760bea"
             :usage/account   #uuid "ee12577c-983f-4729-a0e9-c5789a906c04"
             :usage/duration  60}]))
