#[derive(Debug)]
struct Usuario {
    usuario: String,
    correo: String,
    veces_activo: u64,
    activo: bool
}

#[derive(Debug)]
// Estructura de tipo tupla 
struct Color (i32, i32, i32);

#[derive(Debug)]
struct Rectangulo {
    ancho: u32,
    alto: u32
}

impl Rectangulo {
    fn area(&self) -> u32 {
        self.ancho * self.alto
    }

    fn area2(&self, rect: &Rectangulo) -> u32 {
        println!("Nuestro rectangulo 2 tiene las siguientes dimensiones: {:?}", rect);
        self.ancho * self.alto
    }

    fn rectangulo(ancho: u32, alto: u32) -> Rectangulo {
        Rectangulo {
            ancho,
            alto
        }
    }
}

fn main (){
    let mut usuario1 = Usuario{
        correo: String::from("micorreo@correo.com"),
        usuario: String::from("carlos"),
        veces_activo: 1,
        activo: true
    };

    usuario1.veces_activo = 2;

    println!("{:?}", usuario1);
    println!("{:?}", usuario1.usuario);
    println!("{:?}", usuario1.correo);
    
    let usuario2 = construir_usuario(String::from("arturo@correo.com"), String::from("arturo"));
    println!("{:?}", usuario2);
    
    // Sintaxis de actualizacion
    let usuario3 = Usuario{
        correo: String::from("micorreo2@correo.com"),
        usuario: String::from("carlos2"),
        ..usuario1
    };
    println!("{:?}", usuario3);
    
    let color = Color (3, 25, 42);
    println!("{:?}", color);

    // Estructura con implementacion
    let rect = Rectangulo {
        ancho: 30,
        alto: 50
    };

    let rect2 = Rectangulo {
        ancho: 100,
        alto: 200
    };

    println!("El area del rectangulo es: {:?}", rect.area2(&rect2));
    
    // Funciones asociadas
    let rect3 = Rectangulo::rectangulo(15, 20);
    println!("Nuestro rectangulo 3 tiene las siguientes dimensiones: {:?}", rect3);
    println!("El area del rectangulo 3 es: {:?}", rect3.area());
}

fn construir_usuario (correo: String, usuario: String) -> Usuario {
    Usuario {
        correo,
        usuario,
        veces_activo: 1,
        activo: true
    }
}