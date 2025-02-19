
import axios from "axios";
import React, { useEffect, useState } from "react";
import { Card } from "react-bootstrap";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import getLocalStorageItemWithExpiry from "../util/getLocalStorage";
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { faCopy } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const ShortenURLPage = () =>  {
    const [shortUrl,setShortUrl] = useState("");
    const [longUrl,setLongUrl] = useState("");
    const [errors,setErrors] = useState("");
    const navigate = useNavigate();
    
    const handleCopy = () => {
      console.log('Text copied to clipboard!');
    };

    useEffect(()=>{
      if(!getLocalStorageItemWithExpiry("user")||!getLocalStorageItemWithExpiry("token")){
        navigate("/myurls")
      }
    },[])

   async function LongToShortURL(e) {
                
        e.preventDefault();
        const data = {
            longUrl : longUrl,
            email: getLocalStorageItemWithExpiry("user"),
            
        }
        console.log(data);
        axios.post(`${process.env.REACT_APP_SERVER}/api/v1/urls/`,
             { ...data
            
            },{
            'headers': {
              'Authorization': 'Bearer ' + getLocalStorageItemWithExpiry("token")
            }
          
    })
    .then(response=>{
      if (response.status === 200 || response.status === 201){
            console.log(response.data)
            if (!response.data.shortUrl){
              window.alert("Request failed!Please try again..")
            }
            setShortUrl(response.data.shortUrl);

        }
    })
    .catch(err=>{
      console.log(err.response.data)
      setErrors(err.response.data);
    })
  }
        
  
    return (
    <div className="container" style={{width:"75%"}}>
    <button style={{marginLeft:"75%",marginTop:"15%",backgroundColor:"green"}} className="btn btn-secondary"><Link to="/myurls" style={{textDecoration:"none",color:"whitesmoke"
    }}>My URLs</Link></button>
    <Card style={{marginTop:"3%"}} >
    <div >
      <form className="Auth-form" onSubmit={LongToShortURL}>
        <Card.Header style={{height:"3rem",textAlign:'center'}}>Shorten URL</Card.Header>
        <p style={{color:"red",marginLeft:"2rem"}}>{errors}</p>
          <div className="form-group mt-3" style={{margin:"1.5rem",marginTop:"3rem"}}>
            <label>Enter Long url</label>
            <input
type="url"              className="form-control mt-1"

              placeholder="Enter Long url"
              style={{height:"3rem"}}
              onChange={e => setLongUrl(e.target.value)}
              required
            />
<br></br>
<div>
<label>Shortened url</label>
            <input
              type="text"
              className="form-control mt-1"
              placeholder="Shortened URL"
              style={{height:"3rem"}}
value={shortUrl               }
               disabled
            />
            <div style={{float:"right",marginTop:"-2.3rem"}}>
            <CopyToClipboard text={shortUrl} onCopy={handleCopy}>
          <FontAwesomeIcon  size="1.5x" style={{width:"2rem",cursor:"pointer"}} icon={faCopy}/>
        </CopyToClipboard>
        </div>
        </div>
          </div>
          <div className="d-grid gap-2 mt-3" style={{margin:"1.5rem",height:"3rem"}}>
            <button type="submit" className="btn btn-primary">
              Submit
            </button>
          </div>
          

       
                   </form>
                   
      </div>
    </Card>
    
    {/* <Card style={{marginLeft:"27.5rem",marginTop:"5rem",width:"35rem",height:"2rem"}}>
       <p><b>Your shortened URL</b>: {shortUrl}</p>
    </Card> */}
    </div>
    )
}
export default ShortenURLPage