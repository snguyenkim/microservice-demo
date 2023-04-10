#### Test : Stop Discovery Server 
    Expect: Order Service - POST : PlaceOrder is still working SUCCESSFULLY
    - Start Discovery Server
    - Start Inventory 
    - Start Order
    - PlaceOrder 1
    - FindOrder 1
    - Stop Discovery Server
    - PlaceOrder 2 - SUCCESS
    - FindOrder 2
    Since Order Service still store all info which is provided by DS locally



    