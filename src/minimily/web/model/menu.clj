(ns minimily.web.model.menu)

(def menu-items [{:label "Accounts" 
                  :link "/accounts" 
                  :description "Building awareness of your incomes and spendings is the best way to add value to your money."}

                 {:label "Documents" 
                  :link "/folders" 
                  :description "The archive to organize your digital documents."}

                 {:label "Inventory" 
                  :link "/inventory" 
                  :description "The inventory of all your goods."
                  :submenu [{:label "Locations"
                             :link "/inventory/locations"
                             :description "The locations where you organize your goods."}
                            {:label "Collections"
                             :link "/inventory/collections"
                             :description "The collections you classify your goods."}
                            {:label "Goods"
                             :link "/inventory/goods"
                             :description "All your goods."}]}
                 {:label "Family" 
                  :link "/family" 
                  :description "Manage the data of your entire family."}])