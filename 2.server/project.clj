(defproject exoscale "0.1.0-SNAPSHOT"

  :description "A REST server assignment"
  :url "https://gist.github.com/pyr/6e0247d6530b2a0c0edd63b5d7d80afb"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]

                 [compojure "1.6.0"]
                 [ring/ring-jetty-adapter "1.6.2"]
                 [ring/ring-json "0.4.0"]

                 [cheshire "5.6.3"]]

  :main ^:skip-aot exoscale.core
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})
