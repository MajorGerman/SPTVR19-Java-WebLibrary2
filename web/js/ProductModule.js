class ProductModule{
    
  async printAddProductForm(){
    document.getElementById('context').innerHTML=
    `
    <form id="form" method="POST" enctype="multipart/form-data">
    <div class='text-center justify-content-center'>
        <h2 class="display-5"> Добавить товар </h2>
        <div class="col-md-4 mb-3 mx-auto">
            <label for="exampleInputEmail1" class="form-label">Название</label>
            <input name="name" class="form-control" id="name" required>
        </div>
        <div class="col-md-4 mb-3 mx-auto">
            <label for="exampleInputEmail1" class="form-label">Цена</label>
            <input name="price" type="number" min="1" class="form-control" id="price" required>
        </div>
        <div class="col-md-4 mb-3 mx-auto">
            <label for="exampleInputEmail1" class="form-label">Категория</label>
            <input name="tag" class="form-control" id="tag" required>
        </div>    
        <div class="col-md-4 mb-3 mx-auto">
            <label for="exampleInputEmail1" class="form-label">Описание</label>
            <input name="description" class="form-control" id="description" required>
        </div>    
        <div class="col-md-4 mb-3 mx-auto">
            <label for="exampleInputEmail1" class="form-label">Обложка</label>
            <input name="file" type="file" class="form-control" id="photo" required>
        </div>   
        <div class="col-md-4 mb-3 mx-auto">
            <label for="exampleInputEmail1" class="form-label">Количество</label>
            <input name="count" type="number" class="form-control" id="count" max="999999" min="1" required>
        </div> 
        <br>
        <button id="btn1" type="submit" class="btn btn-primary"> Готово </button>
    </div>
    </form>
    `;
    
    document.getElementById('btn1').addEventListener('click', productModule.addProduct); 
  }
  
  async printBuyProductForm() {
        const response = await fetch('printBuyProductFormJson',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset:utf8'
                }});
        var ids = [];
        var btn_ids = [];
        var counts = [];
        var result = await response.json();
            if (response.ok){
              console.log("Request status: "+result.requestStatus);
              var buy_count = result.buy_count;
            } else {
              console.log("Ошибка получения данных");
            }
        console.log(result)
        if (result.requestStatus == "false") {
            document.getElementById('info').innerHTML = result.info;
            document.getElementById('infobox').style.display = 'block';
        } else {
            document.getElementById('context').innerHTML = `
                <div id="buys" class="row text-center">     
                    <h2 class="display-5"> Каталог </h2> 
                </div>`;           
            for (let product of result) {
                document.getElementById('buys').innerHTML += `
                <div class="col-md mx-auto">
                    <input id="productId${product.id}" type="text" name="productId${product.id}" value="${product.id}" hidden>                
                        <div class="card text-center mx-auto" style="width: 16rem;">
                            <img style="height: 100%;" src="insertFile/${product.cover.path}" class="card-img-top" alt="product">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                                <p class="card-text"><i>${product.cover.description}</i></p>
                                <p class="card-text">${product.price}$ (${product.count} на складе)</p>
                                <input id="c${product.id}" value="${product.count}" hidden>
                                <button id="btn${product.id}a" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal${product.id}">
                                    Купить
                                </button>
                                <div class="modal fade" id="exampleModal${product.id}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="exampleModalLabel"> Подтверждение </h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                Вы уверены, что хотите купить товар "${product.name}" стоимостью ${product.price}$/шт.?
                                            </div>
                                            <div class="modal-footer">
                                                <input type="number" min="1" max="${product.count}" class="form-control" id="buy_count${product.id}" name="buy_count">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">  Нет  </button>
                                                <button id="btn${product.id}" type="submit" class="btn btn-primary">  Да </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                </div>`;

                ids.push(product.id.toString());
        }
        
        for (let j of ids) {
            console.log(j)
            document.getElementById('btn' + j).addEventListener('click', () => { productModule.buyProduct(j, 1) } );
        }
        console.log(ids);

    }
  
  } 
  
  async buyProduct(id, count) {
    const productId = id;
    const buy_count = document.getElementById('buy_count' + id).value;
    const product_data = {
         "productId": productId,
         "buy_count": buy_count
       };
    console.log(product_data)
    const response = await fetch('buyProductJson',{
    method: 'POST',
    body: JSON.stringify(product_data)
        });     
        
    var result = await response.json();
    
    if (response.ok){
      console.log("Request status: " + result.requestStatus);
        document.getElementById('info').innerHTML = result.info;
        document.getElementById('infobox').style.display = 'block';
        console.log("Request status: "+result.requestStatus);
        document.getElementById('context').innerHTML='';
        productModule.printBuyProductForm();
    } else {
      console.log("Ошибка получения данных");
      document.getElementById('infobox').style.display = 'none';
    }
  }
  
  
  async addProduct() {
            const response = await fetch('addProductJson',{
            method: 'POST',
            body: new FormData(document.getElementById('form')),
            });

        var result = await response.json();
            if (response.ok){
              console.log("Request status: " + result.requestStatus);
                document.getElementById('info').innerHTML = result.info;
                document.getElementById('infobox').style.display = 'block';
                console.log("Request status: "+result.requestStatus);
                document.getElementById('context').innerHTML='';
                productModule.printBuyProductForm();
            } else {
              console.log("Ошибка получения данных");
              document.getElementById('infobox').style.display = 'none';
            }
  }
  
  
  async printEditProductForm() {     
        const response = await fetch('printEditProductFormJson',{
            method: 'GET',
            headers: {'Content-Type': 'application/json;charset:utf8'}});
        
        var result = await response.json();
            if (result.requestStatus == "false") {
                document.getElementById('info').innerHTML = result.info;
                document.getElementById('infobox').style.display = 'block';
            } else {
                if (response.ok){
                    document.getElementById('context').innerHTML = `
                        <div class='text-center justify-content-center'>
                        <h2 class="display-5"> Изменить товар </h2>
                        <form id='form'>
                            <select id="idforedit" style="width: 55%;margin: auto;" name="productId" class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                            </select>
                            <div class="col-md-4 mb-3 mx-auto">
                                <label for="exampleInputEmail1" class="form-label">Название</label>
                                <input name="name" class="form-control" id="name">
                            </div>
                            <div class="col-md-4 mb-3 mx-auto">
                                <label for="exampleInputEmail1" class="form-label">Цена</label>
                                <input name="price" type="number" min="1" class="form-control" id="price">
                            </div>
                            <div class="col-md-4 mb-3 mx-auto">
                                <label for="exampleInputEmail1" class="form-label">Категория</label>
                                <input name="tag" class="form-control" id="tag">
                            </div>    
                            <div class="col-md-4 mb-3 mx-auto">
                                <label for="exampleInputEmail1" class="form-label">Описание</label>
                                <input name="description" class="form-control" id="description">
                            </div>    
                            <div class="col-md-4 mb-3 mx-auto">
                                <label for="exampleInputEmail1" class="form-label">Обложка</label>
                                <input name="file" type="file" class="form-control" id="photo">
                            </div> 
                            <div class="col-md-4 mb-3 mx-auto">
                                <label for="exampleInputEmail1" class="form-label">Количество</label>
                                <input name="count" type="number" min="1" class="form-control" id="count">
                            </div> 
                            <div class="col-md-4 mb-3 mx-auto">
                                <button id="btn1" type="submit" class="btn btn-primary"> Изменить </button>   
                            </div>
                            <div class="col-md-4 mb-3 mx-auto">
                                <button id="btn2" type="submit" class="btn btn-danger"> Удалить </button>
                            </div>
                    </form>

                    </div>
    
                    `;}
            }          
                
            document.getElementById("btn1").addEventListener('click', productModule.editProduct); 
            document.getElementById("btn2").addEventListener('click', productModule.deleteProduct);
            
            var jopa = setInterval(productModule.update(result), 50);
           
            for (let prod of result) {
                document.getElementById('idforedit').innerHTML += `
                    <option data-name="${prod.name}" data-price="${prod.price}" value="${prod.id}">${prod.name} (${prod.tags} | ${prod.price}$)</option>
                `;
        
        }

  }
  
    update(result) {
        console.log(result)
        if (document.getElementById("idforedit").value != "") {
            console.log(result[document.getElementById("idforedit").value].name);
            document.getElementById("name").value =  result[document.getElementById("idforedit").value].name;
                //document.getElementById("price").value =  result[document.getElementById("idforedit").value].price;
                //document.getElementById("tag").value = document.getElementById("idforedit").value;
                //document.getElementById("description").value = document.getElementById("idforedit").value;
        }
     }
  
  async editProduct() {
        const response = await fetch('editProductJson',{
        method: 'POST',
        body: new FormData(document.getElementById('form')),
        }); 
        
        var result = await response.json();
            if (response.ok){
                console.log("Request status: " + result.requestStatus);
                document.getElementById('info').innerHTML = result.info;
                document.getElementById('infobox').style.display = 'block';
                document.getElementById('context').innerHTML='';
                productModule.printBuyProductForm();                
            } else {
                console.log("Ошибка получения данных");
                document.getElementById('infobox').style.display = 'none';                    
            }
  }
  
  async deleteProduct() {
        const response = await fetch('deleteProductJson',{
        method: 'POST',
        body: new FormData(document.getElementById('form')),
        });   
        
        var result = await response.json();
            if (response.ok){
                console.log("Request status: " + result.requestStatus);
                document.getElementById('info').innerHTML = result.info;
                document.getElementById('infobox').style.display = 'block';
                document.getElementById('context').innerHTML='';
                productModule.printBuyProductForm();                
            } else {
                console.log("Ошибка получения данных");
                document.getElementById('infobox').style.display = 'none';                    
            }
  }
}
  
const productModule = new ProductModule();
export {productModule};