import React, { useState, useEffect, useContext } from 'react';
import { useParams } from 'react-router-dom';
import { PortContext } from '../hooks/PortContext';
import './DetailPage.css';
import image from '../icon.jpg'; 

const items = [
    {
        id: 1,
        title: '아이템1',
        location: '경기도 수원시 장안구 율전동',
        price: '5,000원',
        imgSrc: image,
        likeCount: 12
    },
    {
        id: 2,
        title: '아이템2',
        location: '경기도 수원시 장안구 율전동',
        price: '5,000원',
        imgSrc: image,
        likeCount: 12
    },
    {
        id: 3,
        title: '아이템3',
        location: '경기도 수원시 장안구 율전동',
        price: '5,000원',
        imgSrc: image,
        likeCount: 12
    },
    {
        id: 4,
        title: '아이템4',
        location: '경기도 수원시 장안구 율전동',
        price: '5,000원',
        imgSrc: image,
        likeCount: 12
    },
    {
        id: 5,
        title: '아이템5',
        location: '경기도 수원시 장안구 율전동',
        price: '5,000원',
        imgSrc: image,
        likeCount: 12
    },
    {
        id: 6,
        title: '아이템6',
        location: '경기도 수원시 장안구 율전동',
        price: '5,000원',
        imgSrc: image,
        likeCount: 12
    }
];

const DetailPage = () => {
    const { id } = useParams();

    const { port } = useContext(PortContext); // Context에서 포트 가져오기

    useEffect(() => {
        const postMessageDetail = () => {
           
                if (port) {
                    console.log("Sending message from DetailPage");
                    port.postMessage(JSON.stringify({"message" : "DetailPage", "Latitude" : "latitude", "Longitude" : "longitude"}));
                } else {
                    console.log("Port is null");
                }
                    
        }
        postMessageDetail();
        
    }, [port]);
    
    const item = items.find(item => item.id === parseInt(id));

    const images = [
        item.imgSrc,
        item.imgSrc,
        item.imgSrc
    ];

    const [currentIndex, setCurrentIndex] = useState(0);

    const handlePrevClick = () => {
        const newIndex = currentIndex === 0 ? images.length - 1 : currentIndex - 1;
        setCurrentIndex(newIndex);
    };

    const handleNextClick = () => {
        const newIndex = currentIndex === images.length - 1 ? 0 : currentIndex + 1;
        setCurrentIndex(newIndex);
    };

    return (
        <div className="content-wrapper">
            <div className="content">
                <div className="image-slider">
                    <button className="prev-button" onClick={handlePrevClick}>❮</button>
                    <img className='content-img' src={images[currentIndex]} alt={item.title} />
                    <button className="next-button" onClick={handleNextClick}>❯</button>
                </div>              
                <div className="profile">
                    <div className="profile-left">
                        <img src="https://d1unjqcospf8gs.cloudfront.net/assets/users/default_profile_80-c649f052a34ebc4eee35048815d8e4f73061bf74552558bb70e07133f25524f9.png" alt="" className="profile-img" />
                        <div className="profile-left-text">
                            <span className='profile-name'>이황근</span>
                            <span className='profile-location'>수원시 장안구 율전동</span>
                        </div>
                    </div>
                    <div className="profile-right">
                        <div className="tp-top">
                            <div className="temperature">
                                <dd className="temp-text">36.5 °C</dd>
                                <div className='temp-bar'>
                                    <div className="temp-bar-inner"></div>
                                </div>
                            </div>
                            <div className='temperature-icon'></div>
                        </div>
                        <span>매너온도</span>
                    </div>
                </div>
                <section className="content-detail">
                    <h1>{item.title}</h1>
                    <p className="content-category">
                        생활가전 ∙ 
                        <time>2일 전</time>
                    </p>
                    <p className="content-price">{item.price}</p>
                    <p className="content-desc">아이템 {item.id}에 대한 설명입니다.</p>
                    <p className="content-counts">관심 {item.likeCount} ∙ 채팅 6 ∙ 조회 193</p>
                </section>
            </div>
        </div>
    );
};

export default DetailPage;
