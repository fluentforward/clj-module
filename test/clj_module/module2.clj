(fn [require-module]
 (let [dep (require-module "module1")
       testfn (:testfn dep)] 
   {    
    :testfn (fn [x]  (* 2 (testfn x)) )
   }
))