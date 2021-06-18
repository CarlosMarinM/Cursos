import './App.css';
import Componente from './components/Componente';
import Estado from './components/Estado';
import Propiedades from './components/Propiedades';
import RenderizadoCondicional from './components/RenderizadoCondicional';
import RenderizadoElementos from './components/RenderizadoElementos';
import { EventosES6, EventosES7 } from './components/Eventos';
import logo from './logo.svg';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <section>
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </section>

        <section>
          {/* Clase 04. Componentes */}
          <Componente msg="Hola soy un componente funcional expresado desde una propiedad" />
          <hr />

          {/* Clase 05. Propiedades */}
          <Propiedades
            cadena="Esto es una cadena de texto"
            numero={25}
            booleano={true}
            arreglo={[1, 2, 3]}
            objeto={{ nombre: "Carlos", correo: "carlosmarin@gmail.com" }}
            funcion={num => num * num}
            elementoReact={<i>Esto es un elemento react</i>}
            componenteReact={<Componente msg="Soy un componente pasado como prop" />} />
          <hr />

          {/* Clase 06. Estado */}
          <Estado />
          <hr />

          {/* Clase 07. Renderizado condicional */}
          <RenderizadoCondicional />
          <hr />

          {/* Clase 08. Renderizado elementos */}
          <RenderizadoElementos />
          <hr />

          {/* Clase 09. Eventos */}
          <EventosES6 />

          {/* Clase 10. Eventos & Property initializers */}
          <EventosES7 />
          <hr />
        </section>
      </header>

    </div>
  );
}

export default App;
