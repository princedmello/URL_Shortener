export default function setLocalStorageItemWithExpiry(key, value, expiryInSeconds) {
    const now = new Date();
    const item = {
      value: value,
      expiry: now.getTime() + expiryInSeconds * 1000 // convert to milliseconds
    };
    localStorage.setItem(key, JSON.stringify(item));
  }
  
  
  
//   // Example usage
//   setLocalStorageItemWithExpiry('myKey', 'myValue', 3600); // Expires in 1 hour
//   const myValue = getLocalStorageItemWithExpiry('myKey');
  