fn main(){
    let mut count = 0;
    let a = loop {
        count += 1;
        if count == 10 {
            break 2 * count;
        }
    };
    println!("{}", a);
    
    count = 0;
    while count < 10 {
        count += 1;
    }
    println!("{}", count);
    
    let arr = [10, 20, 30, 40, 50];
    for item in arr.iter() {
        println!("El valor es: {}", item);
    }
}