(ns clj-module.core
  (require [clojure.java.io :as io])
  )

(def modules (atom {}))

(defn load-module [require-fn module-file]
  (let [modulefn (load-file module-file)]
    (modulefn require-fn) 
    ))

(defn register-module  [module-file data] (swap! modules assoc module-file data) data)

(defn load-module-file [require-fn module-file]
  (let [data (@modules module-file)]
      (if data
        data
        (register-module module-file (load-module require-fn module-file)))))

(defn to-module-paths [module-name prefix]
  [
    (str prefix "/" module-name ".clj")
    (str prefix "/" module-name "/module.clj")
  ])

(defn file-exists [filename]
  (.exists (io/as-file filename)))

(defn dirname [filename] (.getParent (io/as-file filename)))

(defn possible-module-paths [search-paths module-name] (flatten (conj (to-module-paths module-name ".")
      (map (partial to-module-paths module-name) search-paths)
     )))

(defn find-module [search-paths module-name]
  (let [filename (first (filter file-exists (possible-module-paths search-paths module-name)
    ))]
    (if filename [(dirname filename) filename])
  ))

(defn require-module 
  ([module-name] (require-module [] module-name))
  ([search-paths module-name]
    (let [[module-dir module-file] (find-module search-paths module-name)]
      (if module-file (load-module-file (partial require-module (conj search-paths module-dir)) module-file))
      )
    )
  )