fn main(){
    // Los tipos de dato de las variables pueden ser implicitos (el compilador lo detecta, segun el valor de la variable),
    // o explicitos (definiendo el tipo seguido de la declaracion de la variable con :)
    let x: u32 = "10".parse().expect("No es un número");
    println!("{}", x);
    
    // Existen tipos de datos escalares y compuestos
    
    // Variables numericas flotantes
    let y: f64 = 10.0;
    let z: f64 = 30.0;
    println!("{}", y + z);
    
    // Tipos de datos booleanos
    let a: bool = true;
    let b: bool = false;
    println!("{}", a);
    
    // Tipos de datos caracter
    let c = 'a';
    println!("{}", c);
}