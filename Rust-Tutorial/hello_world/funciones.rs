fn main(){
    mi_funcion(3, "hola");
    let x = get_10();
    println!("{}", x);
    scope();
}

fn mi_funcion(x: i32, y: &str){
    println!("El valor de x es : {}", x);
    println!("El valor de y es : {}", y);
}

// Se puede retornar valores en una funcion colocando la linea sin ;
// o se puede especificar la palabra return con el valor a retornar y el ;
fn get_10() -> i32{
    10
}

fn scope(){
    let x = 10;

    let y = {
        let x = 5;
        x * 4
    };

    println!("El valor de x es : {}", x);
    println!("El valor de y es : {}", y);
}