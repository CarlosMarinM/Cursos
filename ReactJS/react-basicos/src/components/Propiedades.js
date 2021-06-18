import React from "react";
import PropTypes from "prop-types";

export default function Propiedades(props) {
    return (
        <div>
            <h2>{props.porDefecto}</h2>
            <ul>
                <li>{props.cadena}</li>
                <li>{props.numero}</li>
                <li>{props.booleano.toString()}</li>
                <li>{props.arreglo.join(", ")}</li>
                <li>{props.objeto.nombre + " - " + props.objeto.correo}</li>
                <li>{props.funcion(6)}</li>
                <li>{props.elementoReact}</li>
                <li>{props.componenteReact}</li>
            </ul>
        </div>
    );
}

Propiedades.defaultProps = {
    porDefecto: "Propiedad por defecto"
}

Propiedades.propTypes = {
    numero: PropTypes.number.isRequired
}