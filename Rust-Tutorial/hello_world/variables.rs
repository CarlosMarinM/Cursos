fn main(){
    // Las variables por defecto son inmutables
    // Para declarar variables mutables se coloca mut despues de let
    let mut x = 10;
    println!("{}", x);
    x = 20;
    println!("{}", x);
    
    const MI_CONSTANTE: u32 = 100_000;
    println!("{}", MI_CONSTANTE);
    
    // Variable sombreada
    let x = 10;
    println!("{}", x);
    let x = x * 2;
    println!("{}", x);
    
    let y = "Hola Mundo!!";
    println!("{}", y);
    let y = y.len();
    println!("{}", y);
    
    let mut z = "Hola Mundo!!";
    println!("{}", y);
    z = z.len();
}