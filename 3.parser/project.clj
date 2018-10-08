(defproject parser "0.1.0-SNAPSHOT"

  :description "ASN.1 parser"
  :url "https://gist.github.com/pyr/6e0247d6530b2a0c0edd63b5d7d80afb"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]]

  :main ^:skip-aot parser.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})
