fn main(){
    let x = 9;
    let mut mensaje;
    if x < 10 {
        mensaje = "El numero es menor que diez";
    } else {
        mensaje = "El numero es mayor o igual que diez";
    }
    println!("{}", mensaje);

    mensaje = if x < 10 {
        "El numero es menor que diez"
    } else {
        "El numero es mayor o igual que diez"
    };
    println!("{}", mensaje);
}