(ns minimily.web.model.menu)

(def menu-items [{:label "Accounting" 
                  :link "/accounting" 
                  :description "Building awareness of your incomes and spendings is the best way to add value to your money."
                  :submenu [{:label "Accounts"
                             :link "/accounting/accounts"
                             :description "Accounts where money is managed."}
                            {:label "Transfers"
                             :link "/accounting/transfers"
                             :description "Transfers between accounts"}
                            {:label "Categories"
                             :link "/accounting/categories"
                             :description "Categories of incomes and expenses."}
                            {:label "Parties"
                             :link "/accounting/third_parties"
                             :description "Parties involved on finantial transactions."}]}

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
                             :description "All your goods."}]}])