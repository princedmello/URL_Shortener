import React from "react";
import { CopyToClipboard } from 'react-copy-to-clipboard';
import { faCopy } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


const Test = () => {
    const textToCopy = 'Text to copy123';
    
    const handleCopy = () => {
      console.log('Text copied to clipboard!');
    };
    
    return (
      <div>
        <CopyToClipboard text={textToCopy} onCopy={handleCopy}>
          <FontAwesomeIcon icon={faCopy}/>
        </CopyToClipboard>
      </div>
    );
  };
  
  export default Test;