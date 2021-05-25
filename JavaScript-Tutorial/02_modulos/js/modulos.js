import saludar, { PI, usuario, Saludo } from "./constantes.js"
import { aritmetica as calculo } from "./aritmetica.js"

console.log("Archivo modulo.js");
console.log(PI, usuario);
console.log(calculo.sumar(3, 4));
console.log(calculo.restar(3, 4));

saludar();
let saludo = new Saludo();

