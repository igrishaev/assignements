(ns parser.core
  (:gen-class)
  (:require [clojure.string  :as str]
            [clojure.java.io :as io]
            [clojure.pprint  :refer [pprint cl-format]])
  (:import java.util.Base64))


(defn base64-extract
  "Fetches a B64 string from a file."
  [path]
  (reduce
   str ""
   (remove
    (fn [line]
      (str/starts-with? line "----"))
    (line-seq (io/reader path)))))


(defn byte->int
  "Turns a signed byte (-128..127) to its normalized integer version (0..255)."
  [^Byte byte]
  (bit-and byte 0xff))

(defn base64-bytes
  "Turns a B64 string into a lazy seq of integers."
  [^String base64string]
  (map
   byte->int
   (seq
    (.decode (Base64/getDecoder)
             (.getBytes base64string)))))


(defn byte->hex
  [number]
  (format "%02X" number))

(defn bytes->hex
  "Renders a seq of integers as a hex string."
  [bytes]
  (apply str (map byte->hex bytes)))


(defn byte->binary
  [number]
  (cl-format nil "~8,'0',B" number))


(defn bytes->binary
  "Renders a seq of integers as a binary string (8 bits per integer)."
  [bytes]
  (apply str (map byte->binary bytes)))


(defn bytes->int
  "Restores a BigInteger from a seq of bytes."
  [bytes]
  (BigInteger.
   ^String
   (bytes->hex bytes)
   16))


;;
;; Turns a pair of tag and its content into Clojure data.
;; Extended below.
;;
(defmulti parse-clj
  (fn [tag content]
    tag))


(defn parse-content
  [len bytes]
  (let [[content bytes] (split-at len bytes)]
    [content bytes]))


(defn head-tail
  [seq]
  [(first seq) (rest seq)])


(defn parse-tag
  [bytes]
  (head-tail bytes))


(defn parse-len
  "Splits a seq of bytes on length and the rest of it."
  [bytes]
  (let [[len bytes] (head-tail bytes)
        short? (< len 128)]

    (if short?
      [len bytes]

      (let [len (- len 128)
            [len-bytes bytes] (split-at len bytes)]
        [(bytes->int len-bytes) bytes]))))


(defn parse-iter
  "Parses a single ASN.1 entity."
  [bytes]

  (let [[tag bytes] (parse-tag bytes)
        [len bytes] (parse-len bytes)
        [content bytes] (split-at len bytes)]

    [tag len content bytes]))


(defn parse-loop
  "Parses all the ASN.1 entities. Returns a vector of them."

  ([bytes]
   (parse-loop bytes []))

  ([bytes result]

   (let [[tag len content bytes] (parse-iter bytes)]
     (let [node (parse-clj tag content)
           result (conj result node)]
       (if (empty? bytes)
         result
         (recur bytes result))))))


(defmethod parse-clj :default
  [tag content]
  {:tag (format "NOT IMPLEMENTED: 0x%02X" tag)
   :attr {}
   :content content})


(defmethod parse-clj 0x30
  [_ content]
  {:tag :seq
   :attr {}
   :content (parse-loop content)})


;;
;; Treat A0 and A1 as sequences, but it's not for sure.
;;
(defmethod parse-clj 0xa0
  [_ content]
  (parse-clj 0x30 content))

(defmethod parse-clj 0xa1
  [_ content]
  (parse-clj 0x30 content))


(defmethod parse-clj 0x02
  [_ content]
  {:tag :int
   :attr {}
   :content (bytes->int content)})


(defmethod parse-clj 0x04
  [_ content]
  {:tag :octet-string
   :attr {}
   :content (bytes->hex content)})


(defmethod parse-clj 0x03
  [_ content]
  {:tag :bit-string
   :attr {}
   :content (bytes->binary content)})


;;
;; Main
;;


(defn -main [& args]
  (if-let [key-path (first args)]

    (-> key-path
        base64-extract
        base64-bytes
        parse-loop
        pprint)

	(binding [*out* *err*]
	  (println "no path given")
	  (System/exit 1))))
