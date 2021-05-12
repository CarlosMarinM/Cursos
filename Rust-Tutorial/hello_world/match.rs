enum TipoDeContacto {
    Personal(String),
    Colega(u64),
    Cliente(String)
}

fn main () {
    // Ejemplo 1 Basico
    let x = 10;
    match x {
        1 => println!("Uno"),
        2 => println!("Dos"),
        3 => println!("Tres"),
        4 => println!("Cuatro"),
        5 => println!("Cinco"),
        _ => println!("No hay coincidencias"),
    }

    // Ejemplo 2 Difrentes patrones
    usando_match (1);
    usando_match (6);
    usando_match (25);
    usando_match (31);

    // Ejemplo 3 Match con retorno de valores
    let boolean = true;
    let mensaje = match boolean {
        false => "Es falso!!",
        true => "Es verdadero!!",
    };
    println!("{} -> {}", boolean, mensaje);

    // Ejemplo 4 Match con uso de Enum
    mostrar_contacto (TipoDeContacto::Colega(34));
    mostrar_contacto (TipoDeContacto::Personal("Correo electronico".to_string()));
}

fn usando_match (x: i32) {
    match x {
        1 => println!("Uno"),
        2 | 4 | 6 | 8 => println!("El número es par"),
        20..=30 => println!("Entre el 20 y el 30"),
        _ => println!("No hay coincidencias"),
    }
}

fn mostrar_contacto (contacto: TipoDeContacto) {
    match contacto {
        TipoDeContacto::Personal(valor) => {
            println!("Personal: {}", valor);
        },
        TipoDeContacto::Colega(valor) => {
            println!("Colega: {}", valor);
        }
        TipoDeContacto::Cliente(valor) => {
            println!("Cliente: {}", valor);
        }
    }
}