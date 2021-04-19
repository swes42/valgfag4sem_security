export default function Home(props) {
    return (
      <div>
        <h2>Home</h2>
        <p>This is home</p>
        <p>{props.id}</p>
      </div>
    );
  }