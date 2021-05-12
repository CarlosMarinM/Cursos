fn main(){
    // Tuplas. Se puede tener una coleccion de datos de diferentes tipos
    let tup = (12, 4.5, "Hola");
    println!("{:?}", tup);

    let mut tup_2: (i32, f32, &str) = (12, 4.5, "Hola");
    println!("{:?}", tup_2);
    
    let (a, b, c) = tup_2;
    println!("{}", a);
    println!("{}", b);
    println!("{}", c);
    
    let d = tup_2.1;
    println!("{}", d);
    
    tup_2.0 = 15;
    println!("{:?}", tup_2);
    
    // Arreglos. Se puede tener una coleccion de datos del mismo tipo
    let mut arr = [15, 1, 32, 65];
    println!("{:?}", arr);
    
    let arr_2: [i32; 5] = [1, 2, 3, 4, 5];
    println!("{:?}", arr_2);

    let arr_3 = [0; 5];
    println!("{:?}", arr_3);
    
    let a = arr[2];
    println!("{}", a);

    arr[1] = 10;
    println!("{:?}", arr);
}