import React , {useEffect, useContext} from 'react';
import { PortContext } from '../hooks/PortContext';
import './MainPage.css';
import { Link } from 'react-router-dom';
import image from '../icon.jpg'; // 이미지 파일을 올바른 경로에서 가져옵니다.

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

const MainPage = () => {
    const { port } = useContext(PortContext); // Context에서 port 가져오기

    useEffect(() => {
        if (port) {
            port.onmessage = function(event) {
                console.log("[PostMessage] Got message: " + event.data);
            };
        }
    }, [port]);

    return (
        <div>
            <div className='result'>
                <div className="result-container">
                    <div className="articles-wrap">
                        <p className="article-kind">인기 중고</p>
                        {items.map(item => (
                            <article key={item.id} className='article-item'>
                                <Link to={`/articles/${item.id}`} className="article-link">
                                    <img className="article-img" src={item.imgSrc} alt={item.title} />
                                    <div className="article-contents">
                                        <span className='article-title'>{item.title}</span>
                                        <p className='article-location'>{item.location}</p>
                                        <p className='article-price'>{item.price}</p>
                                        <section>
                                            <span className='article-like'>
                                                <img className='like-icon' src="https://d1unjqcospf8gs.cloudfront.net/assets/home/base/like-8111aa74d4b1045d7d5943a901896992574dd94c090cef92c26ae53e8da58260.svg" alt="like icon" />
                                                {' ' + item.likeCount + ' '}
                                            </span>
                                        </section>
                                    </div>
                                    
                                </Link>
                            </article>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MainPage;
