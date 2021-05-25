export const PI = Math.PI;

export let usuario = "Carlos";
let password = "contra";

const hello = () => console.log("Hola");
// export default hello;

export default function saludar(){
    console.log("Modulos +ES6 desde la funcion");
}

export class Saludo{
    constructor(){
        console.log("Hola modulos +ES6 desde la clase");
        
    }
}
