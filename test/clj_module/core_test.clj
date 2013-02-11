(ns clj-module.core-test
  (:use clojure.test
        clj-module.core))

(deftest test-basic-module-load
  (testing "Basic module loading"
    
    (testing "Module exports value"
      (is (= "testval" (:testval (require-module "test/clj_module/module1")) ) ))

    (testing "Module exports function"
      (is (= 10 ((:testfn (require-module "test/clj_module/module1")) 9)) ) )
    )
  )

(deftest submodule-loading
  (testing "Including one module from another"
    (is (= 20 ((:testfn (require-module "test/clj_module/module2")) 9)))
    )
  )