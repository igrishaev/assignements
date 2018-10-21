(ns exoscale.storage)

(defn uuid []
  (str (java.util.UUID/randomUUID)))


(defonce __storage (atom nil))


(defn get-job
  [id]
  (get @__storage id))


(defn list-jobs
  []
  (vals @__storage))


(defn put-job
  [job]
  (let [id (uuid)
        job (assoc job :id id)]
    (swap! __storage assoc id job)
    job))


(defn delete-job
  [id]
  (swap! __storage dissoc id))


(defn start
  "An empty event that does nothing for atom storage.
  For SQLite/Datomic, it might initiate the connection."
  [])

(defn stop
  []
  (reset! __storage nil))
