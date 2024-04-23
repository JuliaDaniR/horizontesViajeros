/*Script main*/
const servicios = [
    {
        "image": "https://images.pexels.com/photos/594077/pexels-photo-594077.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"hotel por noche"',
        "codigo": 1,
        "description": "Experimenta el lujo y la comodidad en nuestro hotel, donde cada noche se convierte en una experiencia inolvidable de descanso y hospitalidad."
    },
    {
        "image": "https://images.pexels.com/photos/1592384/pexels-photo-1592384.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"alquiler de auto"',
        "codigo": 2,
        "description": "Vive la libertad de explorar a tu propio ritmo con nuestro servicio de alquiler de autos, donde cada viaje se convierte en una oportunidad para descubrir nuevos destinos."
    },
    {
        "image": "https://images.pexels.com/photos/385997/pexels-photo-385997.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"pasaje de colectivo"',
        "codigo": 3,
        "description": "Viaja con comodidad y seguridad en nuestros servicios de pasaje en colectivo, donde cada trayecto se transforma en un recorrido placentero y sin preocupaciones."
    },
    {
        "image": "https://images.pexels.com/photos/358319/pexels-photo-358319.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"pasaje de avión"',
        "codigo": 4,
        "description": "Descubre la libertad de volar con nuestro servicio de pasaje aéreo, donde cada viaje es una aventura que te acerca a tus destinos soñados."
    },
    {
        "image": "https://images.pexels.com/photos/15536055/pexels-photo-15536055/free-photo-of-edificios-entrenar-tren-vehiculo.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"pasaje de tren"',
        "codigo": 5,
        "description": "Embárcate en una travesía única con nuestro servicio de pasaje en tren, donde cada viaje es una experiencia pintoresca y llena de encanto que te conecta con destinos fascinantes."
    },
    {
        "image": "https://images.pexels.com/photos/15710357/pexels-photo-15710357/free-photo-of-paisaje-montanas-cielo-hombre.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"excursiones"',
        "codigo": 6,
        "description": "Sumérgete en la emoción y la exploración con nuestras excursiones, donde cada paso te lleva a descubrir nuevos horizontes y experiencias inolvidables."
    },
    {
        "image": "https://images.pexels.com/photos/433452/pexels-photo-433452.jpeg?auto=compress&cs=tinysrgb&w=600",
        "name": '"entrada a eventos"',
        "codigo": 7,
        "description": "Sumérgete en la emoción y la diversión con nuestro servicio de entrada a eventos, donde cada acceso te brinda la oportunidad de disfrutar momentos inolvidables llenos de entretenimiento y cultura."
    },
    {
        "image": "https://th.bing.com/th/id/OIP.CeJBTq8DXvXKZgDQWawqHwHaEK?rs=1&pid=ImgDetMain",
        "name": '"paquetes"',
        "codigo": 8,
        "description": "¡Descubre nuestras ofertas exclusivas y elige el paquete perfecto para tus vacaciones! Desde emocionantes combos de pasaje de avión y hotel hasta opciones adicionales para personalizar tu experiencia."
    }
];

// Obtener referencia a los elementos del DOM
const thumbnailsContainer = document.getElementById("thumbnails");
const mainImage = document.getElementById("mainImage");
const serviceName = document.getElementById("serviceName");
const serviceDescription = document.getElementById("serviceDescription");
const prevButton = document.getElementById("prevButton");
const nextButton = document.getElementById("nextButton");
const plusButtonContainer = document.getElementById("plusButtonContainer");

// Inicializar el índice del servicio actual
let currentServiceIndex = 0;

// Función para actualizar la visualización del servicio con animación
function updateServiceView(index) {
    const content = document.getElementById("serviceContent");
    const servicio = servicios[index];

    // Eliminar la animación anterior
    content.style.animation = "none";
    content.offsetHeight; // Trigger reflow para aplicar la eliminación de la animación
    // Volver a aplicar la animación
    content.style.animation = "fadeIn 1s forwards";

    // Actualizar la imagen principal y el contenido del servicio
    mainImage.style.backgroundImage = `url(${servicio.image})`;
    serviceName.innerText = servicio.name;
    serviceDescription.innerText = servicio.description;

    // Actualizar el enlace del botón "+"
    const plusButton = document.getElementById("plusButton");
    plusButton.href = `/servicio/listarPorTipo/${servicio.codigo}`;

    // Remover la clase de animación de todas las miniaturas
    const thumbnails = document.querySelectorAll(".secondary-image-container");
    thumbnails.forEach(thumbnail => thumbnail.classList.remove("show-animation"));
    // Agregar la clase de animación a la miniatura seleccionada
    thumbnails[index].classList.add("show-animation");
}

// Manejador de eventos para el botón "Prev"
prevButton.addEventListener("click", () => {
    currentServiceIndex = (currentServiceIndex - 1 + servicios.length) % servicios.length;
    updateServiceView(currentServiceIndex);
});

// Manejador de eventos para el botón "Next"
nextButton.addEventListener("click", () => {
    currentServiceIndex = (currentServiceIndex + 1) % servicios.length;
    updateServiceView(currentServiceIndex);
});

// Función para crear miniaturas de imágenes secundarias
function createThumbnail(service, index) {
    const thumbnail = document.createElement("div");
    thumbnail.classList.add("secondary-image-container");
    thumbnail.style.backgroundImage = `url(${service.image})`;
    thumbnail.style.backgroundSize = "cover";
    thumbnail.style.backgroundPosition = "center";
    thumbnail.style.width = "50px";
    thumbnail.style.height = "90px";
    thumbnail.addEventListener("click", () => {
        currentServiceIndex = index;
        updateServiceView(index);
    });
    return thumbnail;
}

// Crear miniaturas al cargar la página
servicios.forEach((service, index) => {
    const thumbnail = createThumbnail(service, index);
    thumbnailsContainer.appendChild(thumbnail);
});

// Mostrar el primer servicio al cargar la página
updateServiceView(currentServiceIndex);

// Definir la animación de entrada (fadeIn)
const style = document.createElement("style");
style.innerHTML = `
    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(50px);
        }
        to {
            opacity: 0.9;
            transform: translateY(0);
        }
    }
`;
document.head.appendChild(style);
/*****************************************************************************/
/*script section*/
/* Función para mostrar la parte relativa */
function showDetails(button) {
    var parent = button.closest('.article');
    var relative = parent.querySelector('.relative');
    relative.classList.remove('hidden');
    relative.classList.add('show');
}

/* Función para ocultar la parte relativa */
function hideDetails(button) {
    var parent = button.closest('.article');
    var relative = parent.querySelector('.relative');
    relative.classList.remove('show');
    relative.classList.add('hidden');
}

/*script sectionHome*/
// Datos de los servicios
const responseService = [
    {
        "id_servicio": 2,
        "image": "https://th.bing.com/th/id/OIP._cQPFprkjlbmXSsXDgfKJgHaDw?rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Hotel del Bosque",
        "description": "Sumérgete en la naturaleza en el Hotel del Bosque, situado en medio de exuberantes bosques. Disfruta de actividades al aire libre durante el día y comodidades de lujo por la noche.",
        "service_destination": "Montañas Verdes, Canadá",
        "service_date": "2024-06-15",
        "cost": 180000,
        "service_code": 1,
        "vendedor_id_vendedor": 2,
        "visited": 70
    },
    {
        "id_servicio": 34,
        "image": "https://th.bing.com/th/id/OIP.3mnpZ63xOEaOHOLguDPJzgHaEo?rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Tren Glacier Express de Zermatt a St. Moritz",
        "description": "Embárcate en un viaje panorámico a través de los Alpes suizos con el tren Glacier Express. Disfruta de vistas espectaculares de montañas, valles y glaciares en este recorrido inolvidable.",
        "service_destination": "Zermatt, Suiza - St. Moritz, Suiza",
        "service_date": "2024-09-05",
        "cost": 120000,
        "service_code": 5,
        "vendedor_id_vendedor": 4,
        "visited": 60
    },
    {
        "id_servicio": 51,
        "image": "https://th.bing.com/th/id/OIP.KnAhNf723m72AMWm1ws4OgHaEm?rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Concierto de Coldplay",
        "description": "¡No te pierdas la oportunidad de ver a Coldplay en concierto! Disfruta de una noche llena de música, energía y emoción con una de las bandas más icónicas del mundo.",
        "service_destination": "Estadio Wembley, Londres, Reino Unido",
        "service_date": "2024-07-10",
        "cost": 10000,
        "service_code": 7,
        "vendedor_id_vendedor": 1,
        "visited": 40
    },
    {
        "id_servicio": 6,
        "image": "https://th.bing.com/th/id/OIP.Ks_hUUwQ_Z2W00uI6qpyTAHaE8?rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Hotel Montaña",
        "description": "Escápate a la montaña y disfruta de la tranquilidad en el Hotel Montaña. Rodeado de naturaleza, este hotel es perfecto para los amantes del aire libre.",
        "service_destination": "Montañas Nevadas, Suiza",
        "service_date": "2024-09-05",
        "cost": 250000,
        "service_code": 1,
        "vendedor_id_vendedor": 6,
        "visited": 55
    },
    {
        "id_servicio": 12,
        "image": "https://th.bing.com/th/id/OIP.JlkTLA9clENQsDwOSg_0AAAAAA?rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Ford Mustang Convertible",
        "description": "Siente la emoción de conducir un clásico americano con este Ford Mustang Convertible. Ideal para disfrutar del sol y la brisa en tus viajes.",
        "service_destination": "Miami, Estados Unidos",
        "service_date": "2024-09-10",
        "cost": 120000,
        "service_code": 2,
        "vendedor_id_vendedor": 2,
        "visited": 55
    },
    {
        "id_servicio": 70,
        "image": "https://th.bing.com/th/id/OIP.cGyIU-nYFCwcxEZvkc9wHQHaEK?rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Excursión a la Gran Barrera de Coral",
        "description": "Sumérgete en las aguas cristalinas de la Gran Barrera de Coral en Australia. Explora arrecifes de coral, nados con tiburones y una increíble diversidad marina.",
        "service_destination": "Queensland, Australia",
        "service_date": "2024-09-10",
        "cost": 17000,
        "service_code": 6,
        "vendedor_id_vendedor": 10,
        "visited": 55
    },
    {
        id_servicio: 21,
        image: "https://i.pinimg.com/736x/eb/11/a9/eb11a985ab723315a01e56f436e13d53.jpg",
        created_at: "2024-03-14T00:00:00.000000+00:00",
        name: "Vuelo de Nueva York a París",
        description:
                "¡Experimenta la magia de París con este vuelo directo desde la bulliciosa ciudad de Nueva York! Descubre la belleza de la Ciudad de la Luz y su encanto inigualable.",
        service_destination: "Nueva York, Estados Unidos - París, Francia",
        service_date: "2024-07-10",
        cost: 120000,
        service_code: 4,
        vendedor_id_vendedor: 1,
        visited: 40,
    },
    {
        "id_servicio": 63,
        "image": "https://th.bing.com/th/id/OIP.M1w6he8HmICsyWLXHOn9SAHaIU?w=1024&h=1150&rs=1&pid=ImgDetMain",
        "created_at": "2024-03-14T00:00:00.000000+00:00",
        "name": "Excursión a la Gran Muralla China",
        "description": "Camina sobre la legendaria Gran Muralla China y maravíllate con sus impresionantes vistas. Descubre la historia y la grandeza de esta maravilla arquitectónica.",
        "service_destination": "Pekín, China",
        "service_date": "2024-07-20",
        "cost": 8000,
        "service_code": 6,
        "vendedor_id_vendedor": 3,
        "visited": 50
    }
];
// Función para generar el HTML de cada servicio y agregarlo al contenedor
function renderServicios() {
    const serviciosContainer = document.getElementById('serviciosContainer');
    serviciosContainer.innerHTML = ''; // Limpiar el contenedor antes de agregar nuevos servicios

    responseService.forEach((servicio, index) => {
        const servicioHTML = `
        <div class="card group" style="background-image: url('${servicio.image}')">
            <div class="card-header">
                <h3 class="nombre-servicio">${servicio.name}</h3>
            </div>
            <article class="card-description">
                <div>
                    <h3 class="destino-servicio">Destino: ${servicio.service_destination}</h3>
                </div>
                <div class="container-ver-mas-btn">
                    <button class="ver-mas-btn" onclick="toggleDescription(${index})">Ver más</button>
                </div>
                <div class="descripcion-container" id="descripcion${index}" style="display: none;">
                    <p class="descripcion-servicio">${servicio.description}</p>
                    <div class="container-ver-menos-btn">
                        <button class="ver-menos-btn" onclick="toggleDescription(${index})">Ver menos</button>
                    </div>
                </div>
            </article>
        </div>
    `;
        serviciosContainer.innerHTML += servicioHTML;
    });
}

// Función para mostrar u ocultar la descripción de un servicio
function toggleDescription(index) {
    const descripcion = document.getElementById(`descripcion${index}`);
    descripcion.style.display = descripcion.style.display === 'none' ? 'flex' : 'none';
}

// Llamar a la función para renderizar los servicios al cargar la página
renderServicios();
/***************************************************************************/
/*Script footer*/
function toggleContactForm() {
    var contactFormDiv = document.getElementById("contactForm");
    var closeButton = document.getElementById("closeButton");
    var thankYouMessageDiv = document.getElementById("thankYouMessage");
    if (contactFormDiv.innerHTML === "") {
        var formHTML = '<form onsubmit="mostrarAgradecimiento(event);">' +
                '<h4>Envíanos tu sugerencia</h4>' +
                '<div class="form-group">' +
                '<label for="contactoNombre">Nombre</label></br>' +
                '<input type="text" id="contactoNombre" name="contactoNombre" placeholder="Escribe aquí tu nombre" required />' +
                '</div>' +
                '<div class="form-group">' +
                '<label for="contactoEmail">Email</label></br>' +
                '<input type="email" id="contactoEmail" name="contactoEmail" placeholder="Escribe aquí tu Correo electrónico" required />' +
                '</div>' +
                '<div class="form-group">' +
                '<label for="contactoMensaje">Mensaje</label></br>' +
                '<textarea id="contactoMensaje" name="contactoMensaje" placeholder="Escribe aquí tu sugerencia" rows="5" required></textarea>' +
                '</div>' +
                '<button type="submit">Enviar</button>' +
                '</form>';
        contactFormDiv.innerHTML = formHTML;
        contactFormDiv.style.display = "flex";
        closeButton.style.display = "block"; // Mostrar el botón de cerrar
    } else {
        contactFormDiv.innerHTML = ""; // Limpiar el contenido del formulario
        closeButton.style.display = "none"; // Ocultar el botón de cerrar
    }

    thankYouMessageDiv.style.display = "none"; // Asegurarse de ocultar el mensaje de agradecimiento
}

function closeThankYouMessage() {
    var thankYouMessageDiv = document.getElementById("thankYouMessage");
    thankYouMessageDiv.style.display = "none";
}

function closeForm() {
    var contactFormDiv = document.getElementById("contactForm");
    var closeButton = document.getElementById("closeButton");
    var thankYouMessageDiv = document.getElementById("thankYouMessage");
    contactFormDiv.innerHTML = ""; // Limpiar el contenido del formulario
    closeButton.style.display = "none"; // Ocultar el botón de cerrar
    contactFormDiv.style.display = "none";
    thankYouMessageDiv.style.display = "none"; // Asegurarse de ocultar el mensaje de agradecimiento
}

// Función para simular el mensaje de agradecimiento
function mostrarAgradecimiento(event) {
    event.preventDefault(); // Evitar el comportamiento por defecto del formulario

    var contactFormDiv = document.getElementById("contactForm");
    var contactForm = document.querySelector("#contactForm form");
    var contactoNombre = contactForm.elements.contactoNombre.value;
    var contactoEmail = contactForm.elements.contactoEmail.value;
    var contactoMensaje = contactForm.elements.contactoMensaje.value;
    var thankYouMessageDiv = document.getElementById("thankYouMessage");
    var closeButton = document.getElementById("closeButton");
    // Actualizar el contenido del mensaje de agradecimiento
    var mensaje = `¡Gracias por tu sugerencia, ${contactoNombre}!<br />
                Para más información, ¡regístrate!`;
    thankYouMessageDiv.innerHTML = mensaje;
    // Mostrar el mensaje de agradecimiento
    thankYouMessageDiv.style.display = "block";
    contactFormDiv.style.display = "none";
    closeButton.style.display = "block"; // Mostrar el botón de cerrar
}


//Seleccion pais servicio registro

 