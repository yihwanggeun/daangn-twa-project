import React, { createContext, useState, useEffect } from 'react';

export const PortContext = createContext();

export const PortProvider = ({ children }) => {
    const [port, setPort] = useState(null);

    window.addEventListener("message", function (event) {
        console.log("Received message:", event.data);

        var port = event.ports[0];
        if (typeof port === 'undefined') return;

        setPort(port); // 포트를 Context에 저장

        port.postMessage(JSON.stringify({"message" : "FirstMessage"}));

        port.onmessage = function(event) {
            console.log("[PostMessage] Got message: " + event.data);
        };
    });

    return (
        <PortContext.Provider value={{ port, setPort }}>
            {children}
        </PortContext.Provider>
    );
};
