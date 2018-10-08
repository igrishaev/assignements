(ns parser.core
  (:gen-class)
  (:require [clojure.string  :as str]
            [clojure.java.io :as io]
            [clojure.pprint  :refer [pprint]])
  (:import java.io.RandomAccessFile
           java.nio.ByteBuffer
           javax.xml.bind.DatatypeConverter

           java.util.Base64



           ))


(defn base64-extract
  [path]
  (reduce
   str ""
   (remove
    (fn [line]
      (str/starts-with? line "----"))
    (line-seq (io/reader path)))))



(defn base64-bytes
  [^String base64string]
  (map
   (fn [^Byte byte]
     (bit-and byte 0xff))
   (seq
    (.decode (Base64/getDecoder)
             (.getBytes base64string)))))



(defn parse-asn1
  [^ByteBuffer bb]
  ::nothing-parsed)


(defn bytes->int
  [bytes]
  (BigInteger.

   ^String
   (apply str (for [b bytes]
                (format "%02x" b)))
   16))





(defn parse-content
  [len bytes]
  (let [[content bytes]
        (split-at len bytes)]
    [content bytes]))


(defmulti parse-clj
  (fn [tag content]
    tag))


(defmethod parse-clj 30
  [_ content]

)


(defmethod parse-clj 20
  [_ content]
  (bytes->int content))


(defn head-tail
  [seq]
  [(first seq) (rest seq)])


(defn parse-tag
  [bytes]
  (head-tail bytes))


(defn parse-len
  [bytes]
  (let [[len bytes] (head-tail bytes)
        short? (< len 128)]

    (if short?
      [len bytes]

      (let [len (- len 128)
            [len-bytes bytes]
            (split-at len bytes)]
        [(bytes->int len-bytes) bytes]))))


(defn parse-base
  [bytes]

  (let [[tag bytes] (parse-tag bytes)
        [len bytes] (parse-len bytes)
        [content bytes] (split-at len bytes)]

    [tag len content bytes]

    )

  )





(defn -main [& args]
  (if-let [key-path (first args)]

    (-> key-path
        base64-extract
        base64-bytes
        parse-base
        println)

	(binding [*out* *err*]
	  (println "no path given")
	  (System/exit 1))))
