#[derive(Debug)]
enum Genero {
    Masculino,
    Femenino
}

enum Velocidad {
    Lenta = 10,
    Media = 20,
    Rapida = 30
}

#[derive(Debug)]
enum Value {
    Number(f64),
    Str(String),
    Bool(bool, bool),
    Struct{x: i32, y: i32}
}

enum Option<T> {
    Some(T),
    None
}

#[derive(Debug)]
struct Persona {
    nombre: String,
    genero: Genero
}

fn main(){
    let hombre = Genero::Masculino;
    let mujer = Genero::Femenino;

    println!("{:?}", hombre);
    println!("{:?}", mujer);
    
    let persona = Persona {
        nombre: String::from("Carlos"),
        genero: Genero::Masculino
    };
    println!("{:?}", persona);
    
    // Enum con valores
    let lenta = Velocidad::Lenta;
    let lenta = lenta as u32;
    println!("{:?}", lenta);

    // Enum con multiples valores
    let number = Value::Number(5.6);
    let string = Value::Str(String::from("Hola mundo"));
    let boolean = Value::Bool(true, false);
    let structure = Value::Struct{x: 10, y: 20};

    println!("number {:?} string {:?} boolean {:?} structure {:?}", number, string, boolean, structure);

    // Enum Option<T>
    let numero_some = Some(5);
    let string_some = Some("Hola mundo");
    let string_some2 = Some(String::from("Hola mundo"));

    println!("{:?}", numero_some);
    println!("{:?}", string_some);
    println!("{:?}", string_some2);
}